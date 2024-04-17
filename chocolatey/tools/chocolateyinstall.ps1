$version = '4.3.8'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B9A114D073585CDBBA7CD1C4B4D8C6511D74D07A882F581963961C9082BC3645'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
