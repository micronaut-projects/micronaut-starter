$version = '3.4.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'EC5A7346B660C90D7193F91EF9F4E00CE3AB76298D3666857FEAC2567C543366'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
