$version = '4.10.13'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '87B6F7257F0052E57DEED12B90BBFC10284D17985882FF62CA63569F8CA1578F'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
