$version = '4.10.17'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '29110A74D980A727B35E70422573C43E75799F627A3EBC69884C923F4C741DD7'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
