$version = '4.3.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '962016A696FD015017C5DD0DB228CC4E64911B80F178B31F4C8EB145B3378948'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
