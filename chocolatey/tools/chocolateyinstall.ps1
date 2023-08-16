$version = '3.9.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '514C113D66705CD6C15E1FF5C811FF1F4E4181B98F71D503E9558A72D4D30ED5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
